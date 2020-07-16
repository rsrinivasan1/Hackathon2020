// Load the JSON data.
fetch('positive.json')
  .then(res => res.json())
  .then(data => {
    const margin = 100;
    const width = 1000 - 2 * margin;
    const height = 600 - 2 * margin;

    data = data.sort((a, b) => {
      return (
        a.white +
        a.asian +
        a.black +
        a.hispanic +
        a.unknown -
        (b.white + b.asian + b.black + b.hispanic + b.unknown)
      );
    });

    const races = ['white', 'black', 'asian', 'hispanic', 'unknown'];

    const series = d3.stack().keys(races)(data);

    console.log(series);

    const svg = d3.select('#barChart');

    const gSvg = svg
      .append('g')
      .attr('transform', `translate(${margin}, ${margin})`);

    const xScale = d3
      .scaleBand()
      .domain(
        data.map(function (d) {
          return d.state;
        })
      )
      .range([0, width])
      .padding(0.1);

    const yScale = d3
      .scaleLinear()
      .domain([0, d3.max(series, d => d3.max(d, d => d[1]))])
      .range([height, 0]);

    const colors = d3.schemeTableau10.slice(0, 5);

    const color = d3.scaleOrdinal(colors);

    const rects = gSvg
      .selectAll('g')
      .data(series)
      .enter()
      .append('g')
      .attr('fill', d => color(d.key)); //Color is assigned here because you want everyone for the series to be the same color

    const redLine = gSvg
      .append('line')
      .attr('class', 'line')
      .attr('x1', 0)
      .attr('y1', 0)
      .attr('x2', width)
      .attr('y2', 1)
      .attr('stroke-width', 2)
      .attr('stroke', 'white')
      .attr('opacity', 0);

    rects
      .selectAll('rect')
      .data(d => d)
      .join('rect')
      .attr('x', (d, i) => xScale(d.data.state))
      .attr('y', d => yScale(d[1]))
      .attr('height', d => yScale(d[0]) - yScale(d[1]))
      .attr('width', xScale.bandwidth())
      .on('mouseenter', function (d) {
        redLine
          .attr('opacity', 1)
          .attr(
            'y1',
            yScale(
              d.data.white +
                d.data.asian +
                d.data.black +
                d.data.hispanic +
                d.data.unknown
            )
          )
          .attr(
            'y2',
            yScale(
              d.data.white +
                d.data.asian +
                d.data.black +
                d.data.hispanic +
                d.data.unknown
            )
          );
      })
      .on('mouseleave', function (d) {
        redLine.attr('opacity', 0);
      })
      .on("mouseover", function() { tooltip.style("display", null); })
      .on("mouseout", function(d) {
        tooltip.style("display", "none");
      })
      .on("mousemove", function(d) {
        var xPosition = d3.mouse(this)[0] + 60;
        var yPosition = d3.mouse(this)[1] + 60;
        tooltip.attr("transform", "translate(" + xPosition + "," + yPosition + ")");
        const num = (d.data.white + d.data.asian + d.data.black + d.data.hispanic + d.data.unknown).toLocaleString();
        tooltip.select("text").text(num + ' total');
        
        const selectedE = window.map.selectAll('rect');
        selectedE.style('fill', '#fff'); 

        const selected = window.map
          .selectAll('rect')
          .data([{state: d.data.state}], function (d) {
            return d.state;
          });

          selected.style('fill', 'orange'); 
      });

    // .on("mouseover", function(){d3.select(this).attr("fill", "purple")})
    // .on("mouseout", function(){d3.select(this).attr("fill", color(series.key))})

    const xAxis = gSvg
      .append('g')
      .attr('id', 'xAxis')
      .attr('transform', 'translate(0,' + height + ')')
      .call(d3.axisBottom(xScale));

    const yAxis = gSvg
      .append('g')
      .attr('id', 'yAxis')
      .call(d3.axisLeft(yScale));

    svg
      .append('text')
      .attr('x', -height / 2 - margin)
      .attr('y', margin / 7)
      .attr('transform', 'rotate(-90)')
      .attr('class', 'chart-axis')
      .text('Number positive');

    svg
      .append('text')
      .attr('x', width / 2 + margin)
      .attr('y', 580)
      .attr('class', 'chart-axis')
      .text('State');

    svg
      .append('text')
      .attr('class', 'chart-title')
      .attr('x', width / 2 + margin)
      .attr('y', 40)
      .text('Coronavirus Cases and Race');

    // Draw legend
    var legend = svg
      .selectAll('.legend')
      .data(colors)
      .enter()
      .append('g')
      .attr('class', 'legend')
      .attr('transform', function (d, i) {
        return 'translate(30,' + i * 19 + ')';
      });

    legend
      .append('rect')
      .attr('x', width - 18)
      .attr('width', 18)
      .attr('height', 18)
      .style('fill', function (d, i) {
        return colors.slice().reverse()[i];
      });

    legend
      .append('text')
      .attr('x', width + 5)
      .attr('y', 9)
      .attr('dy', '.35em')
      .attr('fill', '#fff')
      .style('text-anchor', 'start')
      .text(function (d, i) {
        return (
          races[i].charAt(0).toUpperCase() + races[i].substr(1).toLowerCase()
        );
      });

    // Prep the tooltip bits, initial display is hidden
    const tooltip = svg
      .append('g')
      .style('display', 'none');

    tooltip
      .attr('class', 'tooltip')
      .append('rect')
      .attr('width', 70)
      .attr('height', 20)
      .style('opacity', 0.8);

    tooltip
      .append('text')
      .attr('x', 35)
      .attr('dy', '1.2em')
      .attr('fill', 'black')
      .style('text-anchor', 'middle')
      .attr('font-size', '12px')
      .attr('font-weight', 'bold');
  });
